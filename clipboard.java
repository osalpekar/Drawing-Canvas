public class clipboard{

    public clipboard()
    {
    }

    public void storeObject(Object object){
        this.object = object;
    }

    public Object retrieveObject(){
        return object;
    }

    private static final long serialVersionUID = 1L;
    private Object object;
}