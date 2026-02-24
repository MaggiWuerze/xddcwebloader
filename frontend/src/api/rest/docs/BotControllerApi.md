# BotControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createBot**](#createbot) | **POST** /api/v1/bot/ | |
|[**deleteBot**](#deletebot) | **DELETE** /api/v1/bot/{id} | |
|[**getBot**](#getbot) | **GET** /api/v1/bot/{id} | |
|[**listBots**](#listbots) | **GET** /api/v1/bot/ | |
|[**updateBot**](#updatebot) | **PUT** /api/v1/bot/{id} | |

# **createBot**
> BotTO createBot(botFormTO)


### Example

```typescript
import {
    BotControllerApi,
    Configuration,
    BotFormTO
} from './api';

const configuration = new Configuration();
const apiInstance = new BotControllerApi(configuration);

let botFormTO: BotFormTO; //

const { status, data } = await apiInstance.createBot(
    botFormTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **botFormTO** | **BotFormTO**|  | |


### Return type

**BotTO**

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

# **deleteBot**
> string deleteBot()


### Example

```typescript
import {
    BotControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BotControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteBot(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**string**

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

# **getBot**
> BotTO getBot()


### Example

```typescript
import {
    BotControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BotControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getBot(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**BotTO**

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

# **listBots**
> Array<BotTO> listBots()


### Example

```typescript
import {
    BotControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BotControllerApi(configuration);

const { status, data } = await apiInstance.listBots();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<BotTO>**

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

# **updateBot**
> BotTO updateBot(botFormTO)


### Example

```typescript
import {
    BotControllerApi,
    Configuration,
    BotFormTO
} from './api';

const configuration = new Configuration();
const apiInstance = new BotControllerApi(configuration);

let id: string; // (default to undefined)
let botFormTO: BotFormTO; //

const { status, data } = await apiInstance.updateBot(
    id,
    botFormTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **botFormTO** | **BotFormTO**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**BotTO**

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

