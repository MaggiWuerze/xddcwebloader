# ChannelControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createChannel**](#createchannel) | **POST** /channel/ | |
|[**deleteChannel**](#deletechannel) | **DELETE** /channel/{id} | |
|[**getChannel**](#getchannel) | **GET** /channel/{id} | |
|[**listChannels**](#listchannels) | **GET** /channel/ | |
|[**updateChannel**](#updatechannel) | **PUT** /channel/{id} | |

# **createChannel**
> ChannelTO createChannel(channelFormTO)


### Example

```typescript
import {
    ChannelControllerApi,
    Configuration,
    ChannelFormTO
} from './api';

const configuration = new Configuration();
const apiInstance = new ChannelControllerApi(configuration);

let channelFormTO: ChannelFormTO; //

const { status, data } = await apiInstance.createChannel(
    channelFormTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **channelFormTO** | **ChannelFormTO**|  | |


### Return type

**ChannelTO**

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

# **deleteChannel**
> string deleteChannel()


### Example

```typescript
import {
    ChannelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ChannelControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteChannel(
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

# **getChannel**
> ChannelTO getChannel()


### Example

```typescript
import {
    ChannelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ChannelControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getChannel(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**ChannelTO**

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

# **listChannels**
> Array<ChannelTO> listChannels()


### Example

```typescript
import {
    ChannelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ChannelControllerApi(configuration);

const { status, data } = await apiInstance.listChannels();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<ChannelTO>**

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

# **updateChannel**
> ChannelTO updateChannel(channelFormTO)


### Example

```typescript
import {
    ChannelControllerApi,
    Configuration,
    ChannelFormTO
} from './api';

const configuration = new Configuration();
const apiInstance = new ChannelControllerApi(configuration);

let id: string; // (default to undefined)
let channelFormTO: ChannelFormTO; //

const { status, data } = await apiInstance.updateChannel(
    id,
    channelFormTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **channelFormTO** | **ChannelFormTO**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**ChannelTO**

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

