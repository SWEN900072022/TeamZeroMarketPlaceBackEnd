package Entity;

import Injector.IInjector;

import java.util.List;

public abstract class EntityObject {
    private IInjector injector;
    private List<Object> param;

    public IInjector getInjector() {
        return injector;
    }

    public void setInjector(IInjector injector) {
        this.injector = injector;
    }

    public List<Object> getParam() {
        return param;
    }

    public void setParam(List<Object> param) {
        this.param = param;
    }
}
