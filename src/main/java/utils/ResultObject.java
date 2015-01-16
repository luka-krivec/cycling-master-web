package utils;


public class ResultObject {

    private boolean result;
    private String error;

    public ResultObject(boolean result, String error) {
        this.result = result;
        this.error = error;
    }

    public boolean isResultSuccessful() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
