// DÙNG V1 COMPAT 👇
import * as functions from "firebase-functions/v1";
import * as admin from "firebase-admin";

admin.initializeApp();
const db = admin.firestore();

// Khi có review mới -> tính lại avgRating + ratingCount
export const updateRestaurantRating = functions.firestore
  .document("restaurants/{restaurantId}/reviews/{reviewId}")
  .onCreate(
    async (
      snap: functions.firestore.QueryDocumentSnapshot, // khai báo type
      context: functions.EventContext // khai báo type
    ) => {
      const restaurantId = context.params.restaurantId as string;

      const reviewsSnap = await db
        .collection("restaurants")
        .doc(restaurantId)
        .collection("reviews")
        .get();

      let total = 0;
      let count = 0;

      reviewsSnap.forEach((doc) => {
        const data = doc.data() as { rating?: number };
        total += data.rating ?? 0;
        count += 1;
      });

      const avg = count > 0 ? total / count : 0;

      await db.collection("restaurants").doc(restaurantId).update({
        avgRating: avg,
        ratingCount: count,
      });
    }
  );
