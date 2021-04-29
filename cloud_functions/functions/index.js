const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
 
exports.sendHostNotification = functions.database.ref('/Journeys/{journeyUid}/userList/{newUser}')
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

exports.notifyNewJourney = functions.database.ref('/Journeys/{journeyUid}')
    .onCreate(async (change, context) => {
      const journeyUid = context.params.journeyUid;

      const getDeviceTokensPromise = admin.database()
          .ref(`/Users/EGCgh5VZWkMe9BF2HA0SBEOX0j43/deviceToken`).once('value'); 
      let tokensSnapshot;
      const results2 = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results2[0].val();

      const payload = {
        notification: {
          title: 'New trip!',
          body: 'There is a new trip near you! Join the new trip now:)'
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


exports.notifyJourneyEnd = functions.database.ref('/Journeys/{journeyUid}/journeyStatus')
    .onWrite(async (change, context) => {
      const journeyUid = context.params.journeyUid;
      const newUser = context.params.newUser
      const newStatus = change.after.val();
      if (!newStatus.localeCompare("ONGOING")) {
        return functions.logger.log(
          'Journey FINISHED',
          journeyUid
        );
      }
      const getUsersPromise = admin.database().ref(`/Journeys/${journeyUid}/userList`).once('value')
      const results1 = await Promise.all([getUsersPromise]);
      results1.forEach(function(childSnapshot) {
        var key = childSnapshot.key;
        var childData = childSnapshot.val();
        // get participants token
        const getDeviceTokensPromise = admin.database()
          .ref(`/Users/${childData}/deviceToken`).once('value'); 
        const results2 = await Promise.all([getDeviceTokensPromise]);
        let tokensSnapshot;
        tokensSnapshot = results2[0].val();
        functions.logger.log('get host token: ',tokensSnapshot)
        // Notification details.
        const payload = {
          notification: {
            title: 'Journey END!',
            body: 'Your journey is ended. You can now rate your journey'
          }
        };   
        // Send notifications to tokens.
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
    });



exports.notifyJourneyStart = functions.database.ref('/Journeys/{journeyUid}/journeyStatus')
    .onWrite(async (change, context) => {
      const journeyUid = context.params.journeyUid;
      const newUser = context.params.newUser
      const newStatus = change.after.val();
      if (!newStatus.localeCompare("ONGOING")) {
        return functions.logger.log(
          'Journey FINISHED',
          journeyUid
        );
      }
      const getUsersPromise = admin.database().ref(`/Journeys/${journeyUid}/userList`).once('value')
      const results1 = await Promise.all([getUsersPromise]);
      results1.forEach(function(childSnapshot) {
        var key = childSnapshot.key;
        var childData = childSnapshot.val();
        // get participants token
        const getDeviceTokensPromise = admin.database()
          .ref(`/Users/${childData}/deviceToken`).once('value'); 
        const results2 = await Promise.all([getDeviceTokensPromise]);
        let tokensSnapshot;
        tokensSnapshot = results2[0].val();
        functions.logger.log('get host token: ',tokensSnapshot)
        // Notification details.
        const payload = {
          notification: {
            title: 'journey START!',
            body: 'Your journey is now starting. Please go to the starting point.'
          }
        };   
        // Send notifications to tokens.
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
    });

