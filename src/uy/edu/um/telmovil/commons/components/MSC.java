package uy.edu.um.telmovil.commons.components;

import java.util.ArrayList;

import uy.edu.um.telmovil.commons.ConstantesGenerales;
import uy.edu.um.telmovil.commons.ResourceUtils;
import uy.edu.um.telmovil.commons.components.mtp.MTP;
import uy.edu.um.telmovil.msg.IAMMessage;
import uy.edu.um.telmovil.msg.IAM_ERRORMessage;
import uy.edu.um.telmovil.msg.Msg;
import uy.edu.um.telmovil.msg.PRNMessage;
import uy.edu.um.telmovil.msg.PRN_ACKMessage;
import uy.edu.um.telmovil.msg.PRN_ERRORMessage;
import uy.edu.um.telmovil.msg.SRIMessage;
import uy.edu.um.telmovil.msg.SRI_ACKMessage;


/**
 * Esta clase se encarga de verificar si el ID del terminal es borrado
 * @author mcaamano
 *
 */
public class MSC extends MTPUser{

//	private List<RNC> rncs; /// ??? aca deberiamos meter otro mtp???
	
	private VLR vlr;
	
	private MTP mtpToGMSC;
	private MTP mtpToHLR;
	
	private int sendingGMSC;
	private int receivingGMSC;
	
	private int sendingHLR;
	private int receivingHLR;
	
	private String GMSCHost;
	private String HLRHost;
	
	
	private ArrayList<MobilePhone> mobiles;
	
	
	public MSC() {
		//asigno los puertos para envio y recibo de datos de los MTP
		this.receivingHLR= Integer.valueOf(ResourceUtils.obtenerProperty(ResourceUtils.MSC_FROM_HLR_RECEIVING));
		this.receivingGMSC = Integer.valueOf(ResourceUtils.obtenerProperty(ResourceUtils.MSC_FROM_GMSC_RECEIVING));
		this.sendingHLR = Integer.valueOf(ResourceUtils.obtenerProperty(ResourceUtils.MSC_TO_HLR_SENDING));
		this.sendingGMSC = Integer.valueOf(ResourceUtils.obtenerProperty(ResourceUtils.MSC_TO_GMSC_SENDING));

		this.HLRHost=ResourceUtils.obtenerProperty(ResourceUtils.HLR_HOST);
		this.GMSCHost=ResourceUtils.obtenerProperty(ResourceUtils.GMSC_HOST);

		
		// creo cada uno de los MTP
		MTP mtptoHLR = new MTP(this.receivingHLR, this.sendingHLR);
		mtptoHLR.setMtpUser(this);

		MTP mtptoMSC = new MTP(this.receivingGMSC, this.sendingGMSC);
		mtptoMSC.setMtpUser(this);

		// seteo los mtp
		this.mtpToHLR = mtptoHLR;
		this.mtpToGMSC = mtptoMSC;

	}	
	
	
	@Override
	public void MTPTransferIndication(Msg mensaje, long MTP_ID) {
		switch (mensaje.getMsg_type()) {
		case ConstantesGenerales.TIPO_MSG_PRN:
			System.out.println("[GMSC]{Recibi un TIPO_MSG_PRN de <HLR>}");
			//recibi del HLR un PRN
			PRNMessage prn= (PRNMessage) mensaje;
			
			if(prn.getImsi() == null || prn.getImsi().isEmpty())
			{
				PRN_ERRORMessage errorMsg= new PRN_ERRORMessage();
				errorMsg.setMsrn(null);
				errorMsg.setError_code("COD-01_RegistroNoEncontrado");
				this.mtpToHLR.send(HLRHost,errorMsg);
				
			}else{
				PRN_ACKMessage ackMsg= new PRN_ACKMessage();
				ackMsg.setMsrn(prn.getImsi());
				this.mtpToHLR.send(HLRHost,ackMsg);
			}
			
			break;
		case ConstantesGenerales.TIPO_MSG_IAM:
			System.out.println("[MSC]{Recibi un TIPO_MSG_IAM de <GMSC>}");
			
			IAMMessage iam = (IAMMessage) mensaje;
			
			System.out.println("LLAMO AL CELULAR:"+iam.getMsrn());

			break;
			
		}		
	}
	
	public static void main(String[] args) {
		MSC msc = new MSC();
		msc.initializeMTP(msc.mtpToHLR);
		msc.initializeMTP(msc.mtpToGMSC);
		
		System.out.println("[MSC] HOLA!");
	}

}
