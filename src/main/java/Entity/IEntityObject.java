package Entity;

import Injector.ISQLInjector;

import java.util.List;

public interface IEntityObject {
    public ISQLInjector getInjector();
    public void setInjector(ISQLInjector injector);
    public List<Object> getParam();
    public void setParam(List<Object> param);
}
