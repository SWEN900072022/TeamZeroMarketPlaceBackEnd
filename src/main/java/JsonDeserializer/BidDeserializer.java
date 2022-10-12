package JsonDeserializer;

import Domain.Bid;
import com.google.gson.*;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.lang.reflect.Type;

public class BidDeserializer implements JsonDeserializer<Bid> {
    @Override
    public Bid deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Bid bid = Bid.create(
                jsonObject.get("listingId").getAsInt(),
                jsonObject.get("userId").getAsInt(),
                Money.of(
                        jsonObject.get("bidAmountInCents").getAsInt(),
                        Monetary.getCurrency("AUD")
                ).divide(100));
        return bid;
    }
}
