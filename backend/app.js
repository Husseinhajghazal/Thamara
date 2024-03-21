require("dotenv").config({
  path: "config/.env",
});
const express = require("express");
const cors = require("cors");
const helmet = require("helmet");
const compression = require("compression");
const NewError = require("./models/new-error");
const deckRouter = require("./routers/deck-router");
const rateRouter = require("./routers/rate-router");
const tagRouter = require("./routers/tag-router");
const bodyParser = require("body-parser");

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
app.use("/rate", rateRouter);
app.use("/tag", tagRouter);

app.use((req, res, next) => {
  return next(new NewError("Could not find this route.", 404));
});

app.use(async (error, req, res, next) => {
  res.status(error.code || 500);
  res.json({ message: error.message || "An unknown error occurred!" });
});

app.listen(process.env.PORT || 5000, () => {
  console.log(`Server is running on port ${process.env.PORT || 5000}`);
});
