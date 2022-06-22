SELECT USER
FROM DUAL;
--==>>SCOTT

SELECT *
FROM TBL_MEMBER
ORDER BY SID;

TRUNCATE TABLE TBL_MEMBER;
--==>> Table TBL_MEMBER��(��) �߷Ƚ��ϴ�.

SELECT *
FROM TBL_MEMBER
ORDER BY SID;
--==>> ��ȸ ��� ����


--�� CallableStatement �ǽ��� ���� ���ν��� �ۼ�
-- ���ν��� �� : PRC_MEMBERINSERT
-- ���ν��� ��� : PRC_MEMBER ���̺� �����͸� �Է��ϴ� ���ν���
CREATE OR REPLACE PROCEDURE PRC_MEMBERINSERT
( VNAME IN      TBL_MEMBER.NAME%TYPE
, VTEL  IN      TBL_MEMBER.TEL%TYPE
)
IS 
    -- �ֿ� ���� ����
    VSID    TBL_MEMBER.SID%TYPE;
BEGIN
    -- ���� SID �� �ִ밪 ������
    SELECT NVL(MAX(SID),0) INTO VSID
    FROM TBL_MEMBER;
    
    -- ������ �Է�(INSERT ������ ����)
    INSERT INTO TBL_MEMBER(SID, NAME, TEL)
    VALUES((VSID+1), VNAME, VTEL);
    
    --Ŀ��
    COMMIT;
END;

--�� ������ ���ν��� �׽�Ʈ(Ȯ��)
EXEC PRC_MEMBERINSERT('��ȣ��', '010-1111-1111');


--�� ���̺� ��ȸ
SELECT SID, NAME, TEL
FROM TBL_MEMBER
ORDER BY SID;


--�� JDBC08 �� Test001 ���� �� Ȯ��
SELECT *
FROM TBL_MEMBER
ORDER BY SID;
/*
1	��ȣ��	010-1111-1111
2	�̿���	010-4423-0463
3	ȫ�浿	010-1111-1111
4	�̺���	010-2222-3333
*/


--�� CallableStatement �ǽ��� ���� ���ν��� �ۼ�
-- ���ν��� �� : PRC_MEMBERSELECT
-- ���ν��� ��� : TBL_MEMBER ���̺��� �����͸� �о���� ���ν���
-- �� ��SYS_REFCURSOR�� �ڷ����� �̿��Ͽ� Ŀ�� �ٷ��

CREATE OR REPLACE PROCEDURE PRC_MEMBERSELECT
( VRESULT   OUT SYS_REFCURSOR
)
IS 
    -- �ֿ� ���� ����
BEGIN
    OPEN VRESULT FOR
        SELECT SID, NAME, TEL
        FROM TBL_MEMBER
        ORDER BY SID;
        
    --CLOSE VRESULT;    (�ʿ����)
    --COMMIT;           (�ʿ����)
END;
--==>>Procedure PRC_MEMBERSELECT��(��) �����ϵǾ����ϴ�.
