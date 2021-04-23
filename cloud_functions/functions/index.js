const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
 
exports.sendFollowerNotification = functions.database.ref('/Journeys/{journeyUid}/userList/{newUser}')
    .onWrite(async (change, context) => {
      const journeyUid = context.params.journeyUid;
      const newUser = context.params.newUser
      const newUserID = change.after.val();
      // if someone cancels we exit the function
      if (!change.after.val()) {
        return functions.logger.log(
          'Journey ',
          journeyUid
        );
      }
      functions.logger.log(
        'We have a new user UID:',
        journeyUid,
        'for user:',
        newUser,
        'user id:',
        newUserID
      ); 
      //get the host id
      const hostPromise = admin.database().ref(`/Journeys/${journeyUid}/host`).once('value')
      const results = await Promise.all([hostPromise]);
      const hostID = results[0].val();
      functions.logger.log('get host id: ',hostID)
      // Get the list of device notification tokens.
      const getDeviceTokensPromise = admin.database()
          .ref(`/Users/${hostID}/deviceToken`).once('value'); 
      let tokensSnapshot;
      const results2 = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results2[0].val();
      functions.logger.log('get host token: ',tokensSnapshot)
      // Notification details.
      const payload = {
        notification: {
          title: 'You have a new companion!',
          body: `${newUserID} is now joining you.`
        }
      };   
      // Send notifications to all tokens.
      const response = await admin.messaging().sendToDevice(tokensSnapshot, payload);   
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          functions.logger.error(
            'Failure sending notification to',
            tokensSnapshot,
            error
          );
        }
      })
    });
