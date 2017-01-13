CREATE OR REPLACE PROCEDURE SHOW_ALERTS_FOR_PATIENT (
   patient_Id              IN     JSHARDA.PATIENT.PID%TYPE,
   cursor_patient_alerts      OUT SYS_REFCURSOR)
IS
BEGIN
   OPEN cursor_patient_alerts FOR
      SELECT h.ALERT_ID,
             a.ALERT_TYPE,
             d.DISEASE_NAME,
             d.TEST_TYPE,
             h.GENERATED_AT
        FROM JSHARDA.HAS_ALERT h, JSHARDA.ALERT a, JSHARDA.DISEASE d
       WHERE     h.PID = patient_Id
             AND h.STATUS = 'A'
             AND h.ALERT_ID = a.ALERT_ID
             AND a.DID = d.DID;
END;
/
