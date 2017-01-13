CREATE OR REPLACE PROCEDURE SHOW_ALL_PATIENTS_FOR_HS (
   HS_ID             IN     JSHARDA.HEALTH_SUPPORTER.HSID%TYPE,
   cursor_patients      OUT SYS_REFCURSOR)
IS
--
-- Declare program variables as shown below
-- variable_name data_type
BEGIN
   OPEN cursor_patients FOR
      SELECT PATIENT_PID, AUTHDATE, SUPPORTER_TYPE
        FROM JSHARDA.HEALTH_SUPPORTER
       WHERE     HSID = HS_ID
             AND (ENDDATE > CURRENT_TIMESTAMP OR ENDDATE IS NULL);
END;                                                               --Procedure
/
