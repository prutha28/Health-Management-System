CREATE OR REPLACE PROCEDURE SHOW_DIAGNOSIS (
   PATIENT_ID   IN     JSHARDA.PATIENT.PID%TYPE,
   DIAGNOSIS       OUT SYS_REFCURSOR)
IS
--
-- Declare program variables as shown below
-- variable_name data_type
BEGIN
   OPEN DIAGNOSIS FOR
      SELECT UNIQUE (DISEASE_NAME)
        FROM JSHARDA.DISEASE
       WHERE     DISEASE_NAME <> 'General'
             AND DID IN
                    (SELECT DID
                       FROM JSHARDA.HAS_HEALTH_INDICATOR
                      WHERE     PID = PATIENT_ID
                            AND (   ENDED_ON > CURRENT_TIMESTAMP
                                 OR ENDED_ON IS NULL));
END;                                                               --Procedure
/
