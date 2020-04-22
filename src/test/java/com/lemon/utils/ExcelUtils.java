package com.lemon.utils;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;

public class ExcelUtils
{
    
    public static void main(String[] args) throws Exception {
//        List<API> case1 = read(0, 1, API.class);
//        System.out.println(case1);
//        List<Case> case2 = read(1, 1, Case.class);
//        System.out.println(case2);
        //List<API>, list<Case>转换成Object[][]={{api,case}, {api,case},...}
        
        //泛型：服务于集合
        //当泛型定义在类上：只有在创建对象时候，才能指定泛型的类型
        //类中的非静态方法可以使用此泛型,如add
//        ArrayList<String> list = new ArrayList<String>();
//        list.add("a");
//        ArrayList<Integer> list2 = new ArrayList<Integer>();
//        list2.add(1);
     }
    //所有API集合
    public static List<API> apiList;
    //所有Case集合
    public static List<Case> caseList;
    //excel回写集合
    public static List<WriteBackData> wbdList = new ArrayList<>();
    
    /***
     * excel批量回写
     * @TODO
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年4月13日 下午10:07:29
     */
    public static void batchWrite(){
        //本来应该是要把wbdList作为参数传进来，但是因为它是静态的static：因为可以直接拿到，所以就不需要传参数
        //取出所有的数据，放到wbdList
        //1 加载Excel
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(Constants.EXCEL_PATH);
            //2 创建workbook
            Workbook workbook = WorkbookFactory.create(fis);
            //*遍历wbdList,取出sheet/row/cell/contents*
            for(WriteBackData wbd : wbdList)
            {
                //3 获取sheet，4 获取row，5 获取cell
                Sheet sheet = workbook.getSheetAt(wbd.getSheetIndex());
                Row row = sheet.getRow(wbd.getRowNum());
                Cell cell = row.getCell(wbd.getCellNum(), MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                //6 setCellValue
                cell.setCellValue(wbd.getContent());
            }
            //7 回写Excel
            fos = new FileOutputStream(Constants.EXCEL_PATH);
            workbook.write(fos);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            //8 关流
            close(fis);
            close(fos);
        }
    }
    /***
     * 关流方法
     * @TODO 
     * @returnType: void
     * @Author: shuailiuq
     * @DateTime: 2020年4月13日 下午10:44:15
     */
    private static void close(Closeable stream)
    {
        //想让这个close方法即可关输入流、又可以关输出流，怎么处理呢？
        //InputStream/OutputStream没有共同父类，但是都实现了Closeable接口
        //因为InputStream/OutputStream已经有了父类Object，且Java不支持多继承，所以就弄了个父接口Closeable
        try
        {
            if(stream!=null) {
                stream.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /***
     * 
     * @TODO 根据apiId获取对应的api和Case对象
     * @returnType: Object[apiId对应的Case个数][2]
     * @Author: shuailiuq
     * @DateTime: 2020年4月10日 上午11:43:12
     */
    public static Object[][] getAPIAndCaseByApiId(String apiId){
        //需要的一个API
        API wantApi = null;
        //需要的多个Case集合
        List<Case> wantCaseList = new ArrayList<>();
        //1 遍历集合找到符合的API，放wantApi
        for(API api : apiList){
            //找到符合要求的api对象（apiId相等）
            if(apiId.equals(api.getId())) {
                wantApi = api;
                break;
            }
        }
        //2 遍历集合找到需要的Case，放wantCaseList
        for(Case c : caseList) {
          //找到符合要求的case对象（apiId相等）
            if(apiId.equals(c.getApiId())) {
                wantCaseList.add(c);
            }
        }
        //3把API/Case装到Object[apiId对应的Case个数][2]
        Object[][] datas = new Object[wantCaseList.size()][2];
        for(int i=0;i<datas.length;i++) {
            datas[i][0] = wantApi;
            datas[i][1] = wantCaseList.get(i);
        }
        return datas;
    }
    /***
     * 
     * @TODO 读取Excel中的sheet转换成对象的List集合
     * @param <T>   实体类型
     * @param sheetIndex  sheet开始索引
     * @param sheetNum    读取几个sheet
     * @param clazz       实体类型的字节码对象
     * @returnType: List<T>  List<实体类型>
     * @DateTime: 2020年4月8日 下午8:39:36
     */
    public static<T> List<T> read(int sheetIndex, int sheetNum,Class<T> clazz)
    {
        FileInputStream fis = null;
        List<T> list = null;
        try
        {
            fis = new FileInputStream(Constants.EXCEL_PATH);
            ImportParams params = new ImportParams();
            params.setStartSheetIndex(sheetIndex);
            params.setSheetNum(sheetNum);
            list = ExcelImportUtil.importExcel(fis, clazz, params);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } finally {
            close(fis);
        }
        return list;
    }
    
    private static void getAPI() throws FileNotFoundException, Exception
    {
        FileInputStream fis = new FileInputStream("src\\test\\resources\\cases_v3.xlsx");
        //导入参数设置类,params: 从第几个sheet开始导、表头有几行等等
        ImportParams params = new ImportParams();
        params.setStartSheetIndex(0);
        params.setSheetNum(1);
        List<API> importExcel = ExcelImportUtil.importExcel(fis, API.class, params);
        for(API api : importExcel)
        {
            System.out.println(api);
        }
    }
    
    private static void getCase() throws FileNotFoundException, Exception
    {
        FileInputStream fis = new FileInputStream("src\\test\\resources\\cases_v3.xlsx");
        //导入参数设置类,params: 从第几个sheet开始导、表头有几行等等
        ImportParams params = new ImportParams();
        params.setStartSheetIndex(1);
        params.setSheetNum(1);
        List<Case> importExcel = ExcelImportUtil.importExcel(fis, Case.class, params);
        for(Case c : importExcel)
        {
            System.out.println(c);
        }
    }
    //原生poi
    //1打开excel
    //2 创建workbook对象
    //3 获取sheet
    //4 获取row
    //5 获取cell
    public static Object[][] read(){
        try
        {
            FileInputStream fis = new FileInputStream("src\\test\\resources\\cases_v3.xlsx");
            //导入参数设置类,params: 从第几个sheet开始导、表头有几行等等
            ImportParams params = new ImportParams();
            List<API> importExcel = ExcelImportUtil.importExcel(fis, API.class, params);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
