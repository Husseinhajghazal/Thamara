const NewError = require("../model/new-error");
const { validationResult } = require("express-validator");
const Rate = require("../model/rate");

const postRate = async (req, res, next) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }
};

const editRate = async (req, res, next) => {};
