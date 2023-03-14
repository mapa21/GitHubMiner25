package softwaredesign.extraction;

import softwaredesign.Utilitites.NameValue;

public abstract class MultipleData extends Metric{
    protected NameValue<Integer>[] data;
//    protected MultipleData(NameValue<Integer>[] data) { //not sure if this is needed
//        this.data = data;
//    }
    @Override
    protected String contentToString() {
        //convert data array to string
        return null;
    }
}
