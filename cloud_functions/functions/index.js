const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp()

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.helloWorld = functions.https.onRequest((request, response) => {
  functions.logger.info("Hello logs!", {structuredData: true});
  response.send("Hello from Firebase!");
});

// called when new data is written
exports.newNodeDetected = functions.database.ref("/Journeys/")
    .onCreate( (snapshot, context) => {
      const newj = snapshot.val();
      // Notification details.
      const payload = {
        notification: {
          title: 'The Journey List Updates!!!',
          body: `${newj.firstUser} creates a new journey.`,
        }
      };
      let token;
      token = "cASj3iaXSwKBLxhkNdzHMc:APA91bFj7RZ2lHv2iQDpPoSF9ZWkDaN3PX9ElB955ywcl_GHt5nQShCAcUfb_yqlaNSYnkHaFz97w66LGh3-JJNnmSsmdfEyqqHKTu9ZvuLbjrta-2YlUH4qiXd33jG0r07PzrTdEJyN"
      // send notifications to all tokens
      const response = await admin.messaging().sendToDevice(token,payload);
      //For each message check if there was an error
      const tokensToRemove=[];
      response.results.forEach((result, index)) => {
      	const error = result.error;
      }    
    });
