package io.vamp.core.model.notification

import io.vamp.common.notification.Notification


trait WorkflowNotification extends Notification

object UndefinedWorkflowTriggerError extends WorkflowNotification
