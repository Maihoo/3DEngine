package gameClasses;

import shapes.MyTriangle;

public class Quicksort {
	
 
	public static void quickSort(MyTriangle[] arr, int begin, int end, double[] lookAt) {
	    if (begin < end) {
	        int partitionIndex = partition(arr, begin, end, lookAt);
	 
	        quickSort(arr, begin, partitionIndex-1, lookAt);
	        quickSort(arr, partitionIndex+1, end, lookAt);
	    }
	}
 
	private static int partition(MyTriangle[] arr, int begin, int end, double[] lookAt) {
	    MyTriangle pivot = arr[end];
	    int i = (begin-1);
	    
	    double temp =  	Math.sqrt (	Math.pow(pivot.points[0].x - lookAt[0] ,2) + 
									Math.pow(pivot.points[0].y - lookAt[1] ,2) + 
									Math.pow(pivot.points[0].z - lookAt[2] ,2) ) +
	    				Math.sqrt (	Math.pow(pivot.points[1].x - lookAt[0] ,2) + 
	    							Math.pow(pivot.points[1].y - lookAt[1] ,2) + 
	    							Math.pow(pivot.points[1].z - lookAt[2] ,2) ) +
	    				Math.sqrt (	Math.pow(pivot.points[2].x - lookAt[0] ,2) + 
	    							Math.pow(pivot.points[2].y - lookAt[1] ,2) + 
	    							Math.pow(pivot.points[2].z - lookAt[2] ,2) );	
	 
	    for (int j = begin; j < end; j++) {
	        if (Math.sqrt (	Math.pow(arr[j].points[0].x - lookAt[0] ,2) + 
	        				Math.pow(arr[j].points[0].y - lookAt[1] ,2) + 
	        				Math.pow(arr[j].points[0].z - lookAt[2] ,2) ) +
	        	Math.sqrt (	Math.pow(arr[j].points[1].x - lookAt[0] ,2) + 
	        				Math.pow(arr[j].points[1].y - lookAt[1] ,2) + 
	        				Math.pow(arr[j].points[1].z - lookAt[2] ,2) ) +
	        	Math.sqrt (	Math.pow(arr[j].points[2].x - lookAt[0] ,2) + 
        					Math.pow(arr[j].points[2].y - lookAt[1] ,2) + 
        					Math.pow(arr[j].points[2].z - lookAt[2] ,2) ) > temp) {
	            i++;
	 
	            MyTriangle swapTemp = arr[i];
	            arr[i] = arr[j];
	            arr[j] = swapTemp;
	        }
	    }
	 
	    MyTriangle swapTemp = arr[i+1];
	    arr[i+1] = arr[end];
	    arr[end] = swapTemp;
	    
	    /*
	    MyTriangle swapTemp = new MyTriangle(game, arr[i+1].color,
	    	new MyPoint(arr[i+1].points[0].x, arr[i+1].points[0].y, arr[i+1].points[0].z),
	    	new MyPoint(arr[i+1].points[1].x, arr[i+1].points[1].y, arr[i+1].points[1].z),
	    	new MyPoint(arr[i+1].points[2].x, arr[i+1].points[2].y, arr[i+1].points[2].z));
	    
	    arr[i+1] = new MyTriangle(game, arr[end].color,
		    	new MyPoint(arr[end].points[0].x, arr[end].points[0].y, arr[end].points[0].z),
		    	new MyPoint(arr[end].points[1].x, arr[end].points[1].y, arr[end].points[1].z),
		    	new MyPoint(arr[end].points[2].x, arr[end].points[2].y, arr[end].points[2].z));
	    
	    arr[end] = new MyTriangle(game, swapTemp.color,
		    	new MyPoint(swapTemp.points[0].x, swapTemp.points[0].y, swapTemp.points[0].z),
		    	new MyPoint(swapTemp.points[1].x, swapTemp.points[1].y, swapTemp.points[1].z),
		    	new MyPoint(swapTemp.points[2].x, swapTemp.points[2].y, swapTemp.points[2].z));
	    */
	 
	    return i+1;
	}
}