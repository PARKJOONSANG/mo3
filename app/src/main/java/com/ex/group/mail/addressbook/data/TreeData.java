package com.ex.group.mail.addressbook.data;

/**
 * 트리 구조를 위한 정보 클래스
 * @author jokim
 *
 */
public class TreeData {
    public String m_szName; // 이름
    public String m_szUpDept;
    public String m_szDept;
    public int m_nDepth;
    public boolean m_bIsExpanded;
    public boolean m_bIsLastItem;
    public boolean m_bIsVisible;
    public boolean m_bHasChild;
    public String m_szCompanyCd;
}