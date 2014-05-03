package cn.dennishucd.whered.map;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BaiduMap {
	
	/**
     * 绘制单点，该点状态不随地图状态变化而变化
     * @return 点对象
     */
    public static Graphic drawPoint(GeoPoint gp){
	   	//构建点
  		Geometry pointGeometry = new Geometry();
  		//设置坐标
  		pointGeometry.setPoint(gp, 10);
  		//设定样式
  		Symbol pointSymbol = new Symbol();
 		Symbol.Color pointColor = pointSymbol.new Color();
 		pointColor.red = 0;
 		pointColor.green = 126;
 		pointColor.blue = 255;
 		pointColor.alpha = 255;
 		pointSymbol.setPointSymbol(pointColor);
  		//生成Graphic对象
  		Graphic pointGraphic = new Graphic(pointGeometry, pointSymbol);
  		
  		return pointGraphic;
    }

}
