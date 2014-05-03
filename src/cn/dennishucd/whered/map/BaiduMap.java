package cn.dennishucd.whered.map;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BaiduMap {
	
	/**
     * ���Ƶ��㣬�õ�״̬�����ͼ״̬�仯���仯
     * @return �����
     */
    public static Graphic drawPoint(GeoPoint gp){
	   	//������
  		Geometry pointGeometry = new Geometry();
  		//��������
  		pointGeometry.setPoint(gp, 10);
  		//�趨��ʽ
  		Symbol pointSymbol = new Symbol();
 		Symbol.Color pointColor = pointSymbol.new Color();
 		pointColor.red = 0;
 		pointColor.green = 126;
 		pointColor.blue = 255;
 		pointColor.alpha = 255;
 		pointSymbol.setPointSymbol(pointColor);
  		//����Graphic����
  		Graphic pointGraphic = new Graphic(pointGeometry, pointSymbol);
  		
  		return pointGraphic;
    }

}
