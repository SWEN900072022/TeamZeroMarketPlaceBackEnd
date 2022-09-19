package JsonDeserializer;

import Entity.Bid;
import com.google.gson.*;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.lang.reflect.Type;

public class BidDeserializer implements JsonDeserializer<Bid> {
    @Override
    public Bid deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Bid bid = new Bid();
        bid.setListingId(jsonObject.get("listingId").getAsInt());
        bid.setUserId(jsonObject.get("userId").getAsInt());
        bid.setBidAmount(
                Money.of(
                        jsonObject.get("bidAmountInCents").getAsInt(),
                        Monetary.getCurrency("AUD")
                ).divide(100)
        );
        bid.setBidAmountInCents(jsonObject.get("bidAmountInCents").getAsInt());
        return bid;
    }
}
