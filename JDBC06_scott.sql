SELECT USER
FROM DUAL;
--==>>SCOTT

SELECT *
FROM TBL_MEMBER;
--==>>
/*
1	��ȣ��	010-1111-1111
2	�̿���	010-4423-0463
3	��â��	010-4148-0463
4	��ȭ��	010-2511-0463
5	���̻�	010-4444-4444
*/

--�� ������ �Է� ������ ����
INSERT INTO TBL_MEMBER(SID, NAME, TEL)
VALUES(MEMBERSEQ.NEXTVAL, '����', '010-4444-4444');
--> �� �� ����
INSERT INTO TBL_MEMBER(SID, NAME, TEL) VALUES(MEMBERSEQ.NEXTVAL, '����', '010-4444-4444')
;
-->> 1 �� ��(��) ���ԵǾ����ϴ�.

SELECT *
FROM TBL_MEMBER;

--�� Ŀ��
COMMIT;


--�� ������ ��ü ��ȸ ������ ����
SELECT SID, NAME, TEL
FROM TBL_MEMBER
ORDER BY SID;
--> �� �� ����
SELECT SID, NAME, TEL FROM TBL_MEMBER ORDER BY SID
;

/*
1	��ȣ��	010-1111-1111
2	�̿���	010-4423-0463
3	��â��	010-4148-0463
4	��ȭ��	010-2511-0463
5	���̻�	010-4444-4444
6	����	010-4444-4444
7	��浿	010-5555-5555
8	������	010-6666-6666
*/


commit;
-- ���־�~ �̰� ���ؼ� ����� ���� ���� ���δ�~~~




