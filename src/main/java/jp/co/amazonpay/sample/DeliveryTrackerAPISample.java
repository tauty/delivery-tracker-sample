package jp.co.amazonpay.sample;

import com.amazon.pay.api.AmazonPayClient;
import com.amazon.pay.api.AmazonPayResponse;
import com.amazon.pay.api.PayConfiguration;
import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.types.Region;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DeliveryTrackerAPISample {

    public static void main(String[] args) throws IOException, AmazonPayClientException {
        new DeliveryTrackerAPISample().doMain();
    }

    private final Properties prop;

    private DeliveryTrackerAPISample() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileReader("./secret/sample.properties"));
        this.prop = prop;
    }

    public void doMain() throws IOException, AmazonPayClientException {

        AmazonPayResponse response = payClient().deliveryTracker(createPayload());

        System.out.println(response.getStatus());
        System.out.println(response.getHeaders());
        System.out.println(response.getResponse());
    }

    private AmazonPayClient payClient() throws AmazonPayClientException, IOException {
        PayConfiguration payConfiguration = new PayConfiguration()
                .setPublicKeyId(prop.getProperty("publicKeyId"))
                .setRegion(Region.JP)
                .setPrivateKey(new String(Files.readAllBytes(Paths.get("./secret/private.pem"))));
        return new AmazonPayClient(payConfiguration);
    }

    private JSONObject createPayload() {
        JSONObject deliveryDetails = new JSONObject();
        deliveryDetails.put("trackingNumber", prop.getProperty("trackingNumber"));
        deliveryDetails.put("carrierCode", prop.getProperty("carrierCode"));

        JSONArray deliveryDetailsArray = new JSONArray();
        deliveryDetailsArray.add(deliveryDetails);

        JSONObject payload = new JSONObject();
        payload.put("amazonOrderReferenceId", prop.getProperty("amazonOrderReferenceId"));
        payload.put("externalOrderId", prop.getProperty("externalOrderId"));
        payload.put("deliveryDetails", deliveryDetailsArray);

        return payload;
    }
}