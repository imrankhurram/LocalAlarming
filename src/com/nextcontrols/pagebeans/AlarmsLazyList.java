package com.nextcontrols.pagebeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.nextcontrols.bureaudomain.LocalActiveAlarm;

public class AlarmsLazyList extends LazyDataModel<LocalActiveAlarm>  {
	private static final long serialVersionUID = 1L;
	
	private List<LocalActiveAlarm> active_alarms;
  //  private LocalActiveAlarmDAO active_alarmDAO;

    
    public AlarmsLazyList(List<LocalActiveAlarm> active_alarms) {  
        this.active_alarms = active_alarms;  
    }  
      
 /* @Override  
    public LocalActiveAlarm getRowData(String rowKey) {  
        for(LocalActiveAlarm activeAlarm : active_alarms) {  
            if(activeAlarm.getControllerName().equals(rowKey))  
                return activeAlarm;  
        }  
        return null;  
    } */
  
 /* @Override  
      public Object getRowKey(LocalActiveAlarm activealarm) {  
        return activealarm.getControllerName();  
    } */ 
    @Override
	public List<LocalActiveAlarm> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters){
//    @Override  
//    public List<LocalActiveAlarm> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {  
        List<LocalActiveAlarm> data = new ArrayList<LocalActiveAlarm>();  
  
        //filter  
        for(LocalActiveAlarm activealarm : active_alarms) {  
            boolean match = true;  
  
            for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {  
                try {  
                    String filterProperty = it.next();  
                    String filterValue = String.valueOf(filters.get(filterProperty));  
                    String fieldValue = String.valueOf(activealarm.getClass().getField(filterProperty).get(activealarm));  
  
                    if(filterValue == null || fieldValue.startsWith(filterValue)) {  
                        match = true;  
                    }  
                    else {  
                        match = false;  
                        break;  
                    }  
                } catch(Exception e) {  
                    match = false;  
                }   
            }  
  
            if(match) {  
                data.add(activealarm);  
            }  
        }  
  
        //sort  
//        if(sortField != null) {  
        //   Collections.sort(data, new LazySorter(sortField, sortOrder));  
//        }  
  
        //rowCount  
        int dataSize = data.size();  
        this.setRowCount(dataSize);  
  
        //paginate  
        if(dataSize > pageSize) {  
            try {  
                return data.subList(first, first + pageSize);  
            }  
            catch(IndexOutOfBoundsException e) {  
                return data.subList(first, first + (dataSize % pageSize));  
            }  
        }  
        else {  
            return data;  
        }  
    }

 
	
}
