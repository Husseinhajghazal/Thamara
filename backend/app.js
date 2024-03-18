require("dotenv").config({
  path: "config/.env",
});
const express = require("express");
const cors = require("cors");
const helmet = require("helmet");
const compression = require("compression");
const NewError = require("./model/new-error");
const deckRouter = require("./routers/deck-router");
const bodyParser = require("body-parser");
const sequelize = require("./util/database");
const Admin = require("./model/Admin");
const Card = require("./model/Card");
const deck = require("./model/deck");
const rate = require("./model/rate");
const tag = require("./model/tag");

const app = express();

app.use(cors());

app.use(
  helmet({
    crossOriginResourcePolicy: false,
  })
);

app.use(compression());

app.use(express.json());

app.use(bodyParser.urlencoded({ extended: true, limit: "50mb" }));

app.use("/deck", deckRouter);

app.use((req, res, next) => {
  return next(new NewError("Could not find this route.", 404));
});

app.use(async (error, req, res, next) => {
  res.status(error.code || 500);
  res.json({ message: error.message || "An unknown error occurred!" });
});

sequelize
  .sync()
  .then((res) => {
    Admin.sync();
    Card.sync();
    deck.sync();
    rate.sync();
    tag.sync();
    deck.belongsToMany(tag, { through: "DeckTag" });
    deck.hasMany(rate);
    deck.hasMany(Card);
    rate.belongsTo(deck);
    Card.belongsTo(deck);
    app.listen(process.env.PORT || 5000);
  })
  .catch((error) => console.log(error));
