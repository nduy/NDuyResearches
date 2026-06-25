package crossvalidationsampling;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class PicData {
    public String picID= "";
    public String vector= "";
    public String label="";
    PicData(String pID, String pVec, String pLabel){
        picID = pID;
        vector = pVec;
        label = pLabel;
    }
    PicData(){
    }
    
}
