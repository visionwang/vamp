package io.vamp.container_driver.kubernetes

import com.google.gson.reflect.TypeToken
import io.kubernetes.client.models._
import io.vamp.common.akka.CommonActorLogging
import io.vamp.common.util.HashUtil
import io.vamp.container_driver.ContainerDriver

import scala.collection.JavaConverters._
import scala.util.Try

case class KubernetesServicePort(name: String, protocol: String, port: Int)

object KubernetesServiceType extends Enumeration {
  val NodePort, LoadBalancer = Value
}

trait KubernetesService extends KubernetesArtifact {
  this: KubernetesContainerDriver with CommonActorLogging ⇒

  private val nameMatcher = """^[a-z]([-a-z0-9]*[a-z0-9])?$""".r

  protected def services(labels: Map[String, String] = Map()): Seq[V1Service] = {
    val selector = if (labels.isEmpty) null else labelSelector(labels)
    k8sClient.cache.readRequestWithCache(
      K8sCache.services,
      () ⇒ Try(k8sClient.coreV1Api.listNamespacedService(namespace.name, null, null, selector, null, null, null).getItems.asScala).toOption.getOrElse(Nil)
    )
  }

  protected def touchService(service: V1Service): Unit = {
    k8sClient.cache.readRequestWithCache(K8sCache.services, service.getMetadata.getName, () ⇒ service)
  }

  protected def createService(name: String, `type`: KubernetesServiceType.Value, selector: String, ports: List[KubernetesServicePort], update: Boolean, labels: Map[String, String] = Map()): Unit = {
    val id = toId(name)

    def request(): V1Service = {
      val request = new V1Service
      val metadata = new V1ObjectMeta
      request.setMetadata(metadata)
      metadata.setName(id)
      metadata.setLabels(filterLabels(labels).asJava)

      val spec = new V1ServiceSpec
      request.setSpec(spec)
      spec.setSelector(Map(ContainerDriver.labelNamespace() → selector).asJava)
      spec.setType(`type`.toString)
      spec.setPorts(ports.map { p ⇒
        val port = new V1ServicePort
        port.setName(p.name)
        port.setProtocol(p.protocol.toUpperCase)
        port.setPort(p.port)
        port
      }.asJava)
      request
    }

    k8sClient.cache.readRequestWithCache(
      K8sCache.services,
      id,
      () ⇒ k8sClient.coreV1Api.readNamespacedServiceStatus(id, namespace.name, null)
    ) match {
        case Some(_) ⇒
          if (update) {
            log.info(s"Updating service: $name")
            k8sClient.cache.writeRequestWithCache(
              K8sCache.services,
              id,
              () ⇒ k8sClient.coreV1Api.patchNamespacedService(id, namespace.name, k8sClient.coreV1Api.getApiClient.getJSON.serialize(request()), null)
            )
          }
          else log.debug(s"Service exists: $name")

        case None ⇒
          log.info(s"Creating service: $name")
          k8sClient.cache.writeRequestWithCache(
            K8sCache.services,
            id,
            () ⇒ k8sClient.coreV1Api.createNamespacedService(namespace.name, request(), null)
          )
      }
  }

  protected def deleteServiceById(id: String): Unit = {
    log.info(s"Deleting service: $id")
    k8sClient.cache.writeRequestWithCache(
      K8sCache.services,
      id,
      () ⇒ k8sClient.coreV1Api.deleteNamespacedService(id, namespace.name, null)
    )
  }

  protected def createService(request: String): Unit = {
    log.info(s"Creating service")
    k8sClient.coreV1Api.createNamespacedService(
      namespace.name,
      k8sClient.coreV1Api.getApiClient.getJSON.deserialize(request, new TypeToken[V1Service]() {}.getType),
      null
    )
  }

  private def toId(name: String): String = name match {
    case nameMatcher(_*) if name.length < 25 ⇒ name
    case _                                   ⇒ s"hex${HashUtil.hexSha1(name).substring(0, 20)}"
  }
}
