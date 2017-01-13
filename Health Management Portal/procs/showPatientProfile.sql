CREATE OR REPLACE PROCEDURE showPatientProfile (
   PatientID           IN     JSHARDA.PATIENT.PID%TYPE,
   HealthSupporterID   IN     JSHARDA.HEALTH_SUPPORTER.HSID%TYPE,
   P_PID                  OUT JSHARDA.PATIENT.PID%TYPE,
   P_DOB                  OUT PATIENT.DOB%TYPE,
   P_NAME                 OUT PATIENT.NAME%TYPE,
   P_ADDRESS              OUT PATIENT.ADDRESS%TYPE,
   P_GENDER               OUT PATIENT.GENDER%TYPE,
   P_CATEGORY             OUT PATIENT.CATEGORY%TYPE,
   P_CONTACT_NUMBER       OUT PATIENT.CONTACT_NUMBER%TYPE)
IS
--
-- Declare program variables as shown below
-- variable_name data_type
BEGIN
   SELECT PATIENT.PID,
          PATIENT.DOB,
          PATIENT.NAME,
          PATIENT.ADDRESS,
          PATIENT.GENDER,
          PATIENT.CATEGORY,
          PATIENT.CONTACT_NUMBER
     INTO P_PID,
          P_DOB,
          P_NAME,
          P_ADDRESS,
          P_GENDER,
          P_CATEGORY,
          P_CONTACT_NUMBER
     FROM JSHARDA.PATIENT PATIENT, JSHARDA.HEALTH_SUPPORTER HEALTH_SUPPORTER
    WHERE     PATIENT.PID = HEALTH_SUPPORTER.PATIENT_PID
          AND PATIENT.PID = PatientID
          AND HEALTH_SUPPORTER.HSID = HealthSupporterID;

   COMMIT;
END;                                                               --Procedure
/
