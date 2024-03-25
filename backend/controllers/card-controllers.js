const NewError = require("../models/new-error");
const { validationResult } = require("express-validator");

const createCard = async (req, res, next) => {};

const deleteCard = async (req, res, next) => {};

const editCard = async (req, res, next) => {};

const getOneCard = async (req, res, next) => {};

exports.createCard = createCard;

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
