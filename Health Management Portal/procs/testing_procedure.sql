CREATE OR REPLACE PROCEDURE testing_procedure (
   PPPID   IN JSHARDA.PATIENT.PID%TYPE)
IS
   --
   -- Declare program variables as shown below
   -- variable_name data_type
   V   NUMBER;
BEGIN
   --statements;
   TOGGLE_HS_ROLE (PPPID, V);
END;                                                               --Procedure
/
