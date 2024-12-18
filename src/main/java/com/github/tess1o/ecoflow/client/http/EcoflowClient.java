package com.github.tess1o.ecoflow.client.http;

import com.github.tess1o.ecoflow.client.http.response.EcoflowDevice;
import com.github.tess1o.ecoflow.exceptions.EcoflowHttpException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class EcoflowClient {

    private static final String DEVICE_LIST_URL = "/iot-open/sign/device/list";
    private static final String GET_QUOTA_URL = "/iot-open/sign/device/quota";
    private static final String GET_ALL_QUOTA_URL = "/iot-open/sign/device/quota/all";

    private final HttpRestClient restClient;
    private final RestApiResponseParser responseParser;

    public EcoflowClient(HttpRestClient restClient, RestApiResponseParser responseParser) {
        this.restClient = restClient;
        this.responseParser = responseParser;
    }

    public EcoflowClient(HttpRestClient restClient) {
        this.restClient = restClient;
        this.responseParser = new EcoflowRestApiResponseParser();
    }

    public List<EcoflowDevice> getDevices() throws Exception {
        HttpResponse<String> response = restClient.get(DEVICE_LIST_URL);
        if (response.statusCode() != 200) {
            throw new EcoflowHttpException(response.body());
        }
        return responseParser.parseDevicesResponse(response.body()).getData();
    }

    public Map<String, Object> getDeviceAllParameters(String sn) throws Exception {
        JSONObject queryParams = new JSONObject();
        queryParams.put("sn", sn);

        HttpResponse<String> response = restClient.get(GET_ALL_QUOTA_URL, queryParams);
        if (response.statusCode() != 200) {
            throw new EcoflowHttpException(response.body());
        }
        return responseParser.parseParamsResponse(response.body()).getData();
    }

    public Map<String, Object> getDeviceParameters(String sn, List<String> params) throws Exception {
        JSONArray quotas = new JSONArray();
        for (String param : params) {
            quotas.put(param);
        }

        JSONObject paramsObject = new JSONObject();
        paramsObject.put("quotas", quotas);

        JSONObject requestParams = new JSONObject();
        requestParams.put("sn", sn);
        requestParams.put("params", paramsObject);

        HttpResponse<String> response = restClient.post(GET_QUOTA_URL, requestParams);
        if (response.statusCode() != 200) {
            throw new EcoflowHttpException(response.body());
        }
        return responseParser.parseParamsResponse(response.body()).getData();
    }


    public EcoflowPowerStation powerStation() {
        return new EcoflowPowerStation(restClient);
    }
}
