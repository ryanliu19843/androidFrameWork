package com.mdx.framework.server.api.webservice;


//import org.ksoap2.SoapEnvelope;
//import org.ksoap2.serialization.SoapObject;
//import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.HttpTransportSE;
import com.mdx.framework.commons.CanIntermit;
import com.mdx.framework.commons.MException;

public class IntenetRead2WebService implements CanIntermit{
//    private HttpTransportSE ht;
    public IntenetRead2WebService() {
    }
    
    public String get(String NameSpace,String url, String methodname, String[][] params) throws MException {
		return methodname;
//        SoapObject rpc = new SoapObject(NameSpace, methodname);
//        
//        for (int i = 0; i < params.length; i++) {
//            String[] param = params[i];
//            if (param.length > 1) {
//                if (param[0] == null || param[0].length() == 0) {
//                    continue;
//                }
//                if (param[1] == null) {
//                    param[1] = "";
//                }
//                rpc.addProperty(param[0], param[1]);    
//            }
//        }
//       
//        
//        String soupaction = NameSpace + "" + methodname;
//        ht = new HttpTransportSE(url);
//        SoapSerializationEnvelope envelope;
//        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        
//        envelope.bodyOut = rpc;
//        envelope.dotNet = true;
//        envelope.setOutputSoapObject(rpc);
//        try {
//            ht.call(soupaction, envelope);
//            if(envelope.bodyIn instanceof SoapObject){
//                SoapObject sb=(SoapObject)envelope.bodyIn;
//                return sb.getProperty(0).toString();
//            }
//            throw new MException(98, "service error");
//        }
//        catch (Exception e) {
//            throw new MException(98, e.getMessage());
//        }
    }

    
    @Override
    public void intermit() {
    }
    
}
