package site.zido.design;

public class Proxy implements Subject {
    private RealSubject realSubject;

    @Override
    public String request() {
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        preRequest();
        String result = realSubject.request();
        result = postRequest(result);
        return result;
    }

    private String postRequest(String result) {
        return result;
    }

    private void preRequest() {

    }
}
