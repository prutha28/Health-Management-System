CREATE OR REPLACE PROCEDURE TOGGLE_HS_ROLE (
   P_ID   IN     JSHARDA.PATIENT.PID%TYPE,
   RET       OUT INT)
IS
   SUPP_TYPE   CHAR (1);
--
-- Declare program variables as shown below
-- variable_name data_type
BEGIN
   IF (GET_HS_COUNT (P_ID) != 2)
   THEN
      RET := -1;
   ELSE
      UPDATE JSHARDA.HEALTH_SUPPORTER
         SET SUPPORTER_TYPE = 'T'
       WHERE     PATIENT_PID = P_ID
             AND (ENDDATE > CURRENT_TIMESTAMP OR ENDDATE IS NULL)
             AND SUPPORTER_TYPE = 'S';

      UPDATE JSHARDA.HEALTH_SUPPORTER
         SET SUPPORTER_TYPE = 'S'
       WHERE     PATIENT_PID = P_ID
             AND (ENDDATE > CURRENT_TIMESTAMP OR ENDDATE IS NULL)
             AND SUPPORTER_TYPE = 'P';

      UPDATE JSHARDA.HEALTH_SUPPORTER
         SET SUPPORTER_TYPE = 'P'
       WHERE     PATIENT_PID = P_ID
             AND (ENDDATE > CURRENT_TIMESTAMP OR ENDDATE IS NULL)
             AND SUPPORTER_TYPE = 'T';


      RET := 1;
   END IF;
END;                                                               --Procedure
/
