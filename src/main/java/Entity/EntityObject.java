package Entity;

import Injector.ISQLInjector;

import java.util.List;

public abstract class EntityObject implements IEntityObject {
    private ISQLInjector injector;
    private List<Object> param;

    public ISQLInjector getInjector() {
        return injector;
    }

    public void setInjector(ISQLInjector injector) {
        this.injector = injector;
    }

    public List<Object> getParam() {
        return param;
    }

    public void setParam(List<Object> param) {
        this.param = param;
    }
}
