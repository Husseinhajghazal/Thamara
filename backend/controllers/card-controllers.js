const NewError = require("../models/new-error");
const { validationResult } = require("express-validator");
const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

const createCard = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { deck_id } = req.body;

  let deck;
  try {
    deck = await prisma.deck.findUnique({
      where: { id: parseInt(deck_id) },
    });
  } catch (e) {
    return next(
      new NewError(
        "حصلت مشكلة أثناء الحصول على المجموعة, الرجاء المحاولة لاحقا",
        500
      )
    );
  }

  if (!deck) {
    return next(
      new NewError("لا يجود مجموعة بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  const createdCard = await prisma.card.create({
    data: {
      ...req.body,
    },
  });

  res.status(201).json({
    message: "تم أنشاء الكرت بنجاح",
    createdCard,
  });
};

const deleteCard = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let deletedCard;
  try {
    deletedCard = await prisma.card.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!deletedCard) {
    return next(
      new NewError("لا يجود كرت بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  try {
    await prisma.card.delete({
      where: {
        id,
      },
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  res.json({
    message: "تم حذف الكرت بنجاح",
    deletedCard,
  });
};

const editCard = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { deck_id } = req.body;
  const id = parseInt(req.params.id);

  let deck;
  try {
    deck = await prisma.deck.findUnique({
      where: { id: parseInt(deck_id) },
    });
  } catch (e) {
    return next(
      new NewError(
        "حصلت مشكلة أثناء الحصول على المجموعة, الرجاء المحاولة لاحقا",
        500
      )
    );
  }

  if (!deck) {
    return next(
      new NewError("لا يجود مجموعة بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  let card;
  try {
    card = await prisma.card.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!card) {
    return next(
      new NewError("لا يجود كرت بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  const updatedCard = await prisma.card.update({
    where: { id },
    data: {
      ...card,
      ...req.body,
    },
  });

  res.status(201).json({
    message: "تم تعديل الكرت بنجاح",
    updatedCard,
  });
};

const getOneCard = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let card;
  try {
    card = await prisma.card.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!card) {
    return next(
      new NewError("لا يجود كرت بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  res.json({
    message: "تم الحصول الكرت بنجاح",
    card,
  });
};

exports.createCard = createCard;
exports.deleteCard = deleteCard;
exports.editCard = editCard;
exports.getOneCard = getOneCard;

// const lines = [
//   {
//     price: 34.99,
//     unitSellingPrice: 40.99,
//     productId: 161162,
//     name: "İkili Hamburger Menü",
//     items: [
//       {
//         packageItemId: "1000000021680", // UnSupplied servisinde kullanılacak
//         lineItemId: 1000000069963,
//         isCancelled: false,
//         promotions: [
//           {
//             promotionId: 1949565,
//             description: "X TL Indirim", // Promosyon açıklaması
//             discountType: "INSTANT_DISCOUNT", // İndirim tipi
//             sellerCoverageRatio: 0.0, // Satıcı tarafından karşılanan oran
//             amount: {
//               seller: 0.0, // Satıcı tarafından karşılanan tutar
//             },
//           },
//         ],
//         coupon: {
//           couponId: "f61369c8-55fa-4d42-ad20-2c71e6b3646d",
//           description: "X İndirim", // Kupon açıklaması
//           sellerCoverageRatio: 0.0, // Satıcı tarafından karşılanan oran
//           amount: {
//             seller: 0.0, // Satıcı tarafından karşılanan tutar
//           },
//         },
//       },
//     ],

//     modifierProducts: [
//       {
//         name: "Et Burger",
//         price: 0.0,
//         productId: 161167,
//         modifierGroupId: 19900,
//         modifierProducts: [
//           {
//             name: "3 Ekstra Peynir",
//             price: 6.0,
//             productId: 161463,
//             modifierGroupId: 19926,
//             modifierProducts: [],
//             extraIngredients: [],
//             removedIngredients: [],
//           },
//         ],
//         extraIngredients: [
//           {
//             id: 25720,
//             name: "Domates",
//             price: 0.0,
//           },
//         ],
//         removedIngredients: [
//           {
//             id: 25730,
//             name: "Soğan",
//           },
//         ],
//       },
//       {
//         name: "Tavuk Burger",
//         price: 0.0,
//         productId: 160933,
//         modifierGroupId: 19899,
//         modifierProducts: [
//           {
//             name: "60 gr.",
//             price: 0.0,
//             productId: 789,
//             modifierGroupId: 212,
//             modifierProducts: [],
//             extraIngredients: [],
//             removedIngredients: [],
//           },
//         ],
//         extraIngredients: [
//           {
//             id: 25728,
//             name: "Tursu",
//             price: 0.0,
//           },
//         ],
//         removedIngredients: [
//           {
//             id: 25721,
//             name: "Göbek Salata",
//           },
//         ],
//       },
//       {
//         name: "Patates Kızartması (Büyük Boy)",
//         price: 0.0,
//         productId: 160946,
//         modifierGroupId: 19852,
//         modifierProducts: [
//           {
//             name: "Acı Sos",
//             price: 0.0,
//             productId: 160973,
//             modifierGroupId: 19949,
//             modifierProducts: [],
//             extraIngredients: [],
//             removedIngredients: [],
//           },
//         ],
//         extraIngredients: [],
//         removedIngredients: [],
//       },
//     ],
//     extraIngredients: [],
//     removedIngredients: [],
//   },
// ];
