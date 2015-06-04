---
title: Usage
weight: 20
---
menu:
  main:
    parent: cli

# Usage

VAMP has a command line interface (CLI) which can be used to perform some basic actions.

VAMP CLI needs to know where the VAMP Host is locate. The host can be specified as a command line option (--host) or via an environment variable (VAMP_HOST) 
Example:
{{% copyable %}}
```bash
export VAMP_HOST=http://192.168.59.103:8081
```
{{% /copyable %}}

The command line parameter will overrule the environment variable. With the exception of the `help` and the `version`, all commands require the Vamp host to be specified.

VAMP CLI support the following commands:
                       
* [clone-breed](#clone-breed)               
* [create](#create)  
* [deploy-breed](#deploy-breed)                 
* [filters](#filters)                                     
* [help](#help)                              
* [info](#info)                              
* [inspect](#inspect)  
* [list](#list)                  
* [remove](#remove)                        
* [version](#version)  

For more details about a specific command, try `vamp COMMAND --help`
                     

## <a name="clone-breed"></a>Clone Breed

Clones an existing breed

**Usage:** vamp clone-breed NAME --destination [--deployable] 

Parameter | purpose
----------|--------
--destination   |   Name of the new breed
--deployable    |   Name of the deployable [Optional]
### Example
```bash
```

## <a name="create"></a>Create

Create an artifact read from the specified filename. When no file name is supplied, stdin will be read.

**Usage:** vamp create breed NAME [--file] 

Parameter | purpose
----------|--------
  --file        |       Name of the yaml file [Optional]
### Example
```bash
```


## <a name="deploy-breed"></a>Deploy Breed

Deploys a breed into an existing deployment cluster

**Usage:** vamp deploy-breed NAME --deployment --cluster --routing --scale 

Parameter | purpose
----------|--------
  --deployment   |      Name of the existing deployment
  --cluster      |      Name of the cluster within the deployment
  --routing      |      Name of the routing to apply [Optional]
  --scale        |      Name of the scale to apply [Optional]

### Example
```bash
```



## <a name="help"></a>Help
```bash
> vamp help
**Usage:** vamp COMMAND [args..]

Commands:
  clone-breed         Clone a breed
  create              Create an artifact
  deploy-breed        Deploy a breed into an existing deployment cluster
  help                This message
  info                Information from Vamp Core
  inspect             Shows the details of the specified artifact
  list                Shows a list of artifacts
  remove              Removes an artifact
  version             Show version of the VAMP CLI client

Run vamp COMMMAND --help  for additional help about the different command options
```



## <a name="info"></a>Info

Displays the Vamp Info message

### Example
```bash
> vamp info
message: Hi, I'm Vamp! How are you?
jvm:
  operating_system:
    name: Mac OS X
    architecture: x86_64
    version: 10.9.5
    available_processors: 8.0
    system_load_average: 4.8095703125
  runtime:
    process: 12871@MacMatthijs-4.local
    virtual_machine_name: Java HotSpot(TM) 64-Bit Server VM
    virtual_machine_vendor: Oracle Corporation
    virtual_machine_version: 25.31-b07
    start_time: 1433415167162
    up_time: 1305115
...    
```

## <a name="inspect"></a>Inspect
Shows the details of the specified artifact

**Usage:** vamp inspect blueprint|breed|deployment|escalation|filter|routing|scale|sla NAME --json  

| Parameter | purpose |
|-----------|---------|
| --json    |  Output Json instead of Yaml[Optional]|

### Example
```bash
> vamp inspect breed sava:1.0.0
name: sava:1.0.0
deployable: magneticio/sava:1.0.0
ports:
  port: 80/http
environment_variables: {}
constants: {}
dependencies: {}
```

## <a name="list"></a>List
Shows a list of artifacts

**Usage:** vamp list blueprints|breeds|deployments|escalations|filters|routings|scales|slas  

### Example
```bash
> vamp list deployments
NAME                                    CLUSTERS
80b310eb-027e-44e8-b170-5bf004119ef4    sava
06e4ace5-41ce-46d7-b32d-01ee2c48f436    sava
a1e2a68b-295f-4c9b-bec5-64158d84cd00    sava, backend1, backend2
```

## <a name="remove"></a>Remove

Removes artifact

**Usage:** vamp remove breed NAME 

### Example
```bash
> vamp remove breed fds
```

## <a name="version"></a>Version

Displays the Vamp CLI version information 

### Example
```bash
> vamp version
CLI version: 0.7.7
```