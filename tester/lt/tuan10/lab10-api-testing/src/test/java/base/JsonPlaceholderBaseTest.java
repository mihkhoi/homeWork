package base;

public
class JsonPlaceholderBaseTest extends ApiBaseTest {
    @Override protected String getBaseUri() {
        return "https://jsonplaceholder.typicode.com";
    }

    @Override protected String getBasePath() {
        return "";
    }

    @Override protected boolean shouldSendReqresApiKey() {
        return false;
    }
}
