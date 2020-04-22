package com.lemon.utils;

public class WriteBackData
{
    //回写Excel sheetIndex
    private int sheetIndex;
    //回写Excel行号
    private int rowNum;
    //回写Excel列号
    private int cellNum;
    //回写Excel内容
    private String content;
    
    public WriteBackData()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    public WriteBackData(int sheetIndex, int rowNum, int cellNum, String content)
    {
        super();
        this.sheetIndex = sheetIndex;
        this.rowNum = rowNum;
        this.cellNum = cellNum;
        this.content = content;
    }
    public int getSheetIndex()
    {
        return sheetIndex;
    }
    public void setSheetIndex(int sheetIndex)
    {
        this.sheetIndex = sheetIndex;
    }
    public int getRowNum()
    {
        return rowNum;
    }
    public void setRowNum(int rowNum)
    {
        this.rowNum = rowNum;
    }
    public int getCellNum()
    {
        return cellNum;
    }
    public void setCellNum(int cellNum)
    {
        this.cellNum = cellNum;
    }
    public String getContent()
    {
        return content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }
    @Override
    public String toString()
    {
        return "WriteBackData [sheetIndex=" + sheetIndex + ", rowNum=" + rowNum + ", cellNum=" + cellNum + ", content="
                + content + "]";
    }
    
}
