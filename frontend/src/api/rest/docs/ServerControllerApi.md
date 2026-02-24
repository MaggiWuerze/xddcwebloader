# ServerControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createServer**](#createserver) | **POST** /api/v1/server/ | |
|[**deleteServer**](#deleteserver) | **DELETE** /api/v1/server/{id} | |
|[**getServer**](#getserver) | **GET** /api/v1/server/{id} | |
|[**listServers**](#listservers) | **GET** /api/v1/server/ | |
|[**updateServer**](#updateserver) | **PUT** /api/v1/server/{id} | |

# **createServer**
> ServerTO createServer(serverFormTO)


### Example

```typescript
import {
    ServerControllerApi,
    Configuration,
    ServerFormTO
} from './api';

const configuration = new Configuration();
const apiInstance = new ServerControllerApi(configuration);

let serverFormTO: ServerFormTO; //

const { status, data } = await apiInstance.createServer(
    serverFormTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **serverFormTO** | **ServerFormTO**|  | |


### Return type

**ServerTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteServer**
> object deleteServer()


### Example

```typescript
import {
    ServerControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ServerControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteServer(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getServer**
> ServerTO getServer()


### Example

```typescript
import {
    ServerControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ServerControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getServer(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**ServerTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listServers**
> Array<ServerTO> listServers()


### Example

```typescript
import {
    ServerControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ServerControllerApi(configuration);

const { status, data } = await apiInstance.listServers();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<ServerTO>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateServer**
> ServerTO updateServer(serverFormTO)


### Example

```typescript
import {
    ServerControllerApi,
    Configuration,
    ServerFormTO
} from './api';

const configuration = new Configuration();
const apiInstance = new ServerControllerApi(configuration);

let id: string; // (default to undefined)
let serverFormTO: ServerFormTO; //

const { status, data } = await apiInstance.updateServer(
    id,
    serverFormTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **serverFormTO** | **ServerFormTO**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**ServerTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

