package service;

public class ServiceEmptyResponse {
    private int status;
    private String errorMessage;

    public ServiceEmptyResponse(int status, String errorMessage){
        this.status=status;
        this.errorMessage = errorMessage;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
