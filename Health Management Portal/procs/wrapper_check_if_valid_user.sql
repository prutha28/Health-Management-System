CREATE OR REPLACE PROCEDURE wrapper_check_if_valid_user (
   PATIENT_ID     IN     JSHARDA.PATIENT.PID%TYPE,
   LOGGED_IN_ID   IN     JSHARDA.PATIENT.PID%TYPE,
   ret               OUT NUMBER)
IS
   --
   validity   BOOLEAN;
-- Declare program variables as shown below
-- variable_name data_type
BEGIN
   ret := 0;

   IF (CHECK_IF_USER_VALID (PATIENT_ID, LOGGED_IN_ID))
   THEN
      ret := 1;
   END IF;
END;                                                               --Procedure
/
