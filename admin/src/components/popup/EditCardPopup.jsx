"use client";

import React, { useEffect, useState } from "react";

const EditCardPopup = ({
  id,
  oldQuestion,
  oldAnswer_type,
  oldImage_url,
  oldRight_answer,
  deck_id,
  oldWrongAnswer1,
  oldWrongAnswer2,
  oldWrongAnswer3,
}) => {
  const [question, setQuestion] = useState("");
  const [queType, setQueType] = useState("tf");
  const [image_url, setImage_url] = useState("");
  const [rightAnswer, setRightAnswer] = useState(true);
  const [wrongAnswer1, setWrongAnswer1] = useState("");
  const [wrongAnswer2, setWrongAnswer2] = useState("");
  const [wrongAnswer3, setWrongAnswer3] = useState("");

  useEffect(() => {
    setQuestion(oldQuestion);
    setQueType(oldAnswer_type);
    setImage_url(oldImage_url);
    setRightAnswer(oldRight_answer);
    setWrongAnswer1(oldWrongAnswer1);
    setWrongAnswer2(oldWrongAnswer2);
    setWrongAnswer3(oldWrongAnswer3);
  }, []);

  const handleSumbit = async (e) => {
    e.preventDefault();
    fetch("https://one18-team.onrender.com/card/" + id, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        question,
        image_url,
        answer_type: queType,
        right_answer_tf: queType == "tf" ? rightAnswer : null,
        right_answer_mc: queType == "mc" ? rightAnswer : null,
        right_answer_s: queType == "s" ? rightAnswer : null,
        mc_choice_1: wrongAnswer1,
        mc_choice_2: wrongAnswer2,
        mc_choice_3: wrongAnswer3,
        deck_id,
      }),
    })
      .then((response) => response.json())
      .then((data) => window.location.reload())
      .catch((e) => console.log(e));
  };

  return (
    <div className="bg-white p-6 rounded-md absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex items-center justify-center gap-5 flex-col">
      <h2 className="text-green-500 font-semibold text-3xl">Edit Card</h2>
      <form onSubmit={handleSumbit} className="flex flex-col gap-3">
        <div className="bg-gray-200 rounded-lg overflow-hidden">
          <button
            type="button"
            className={` ${
              queType == "tf" ? "bg-green-500 p-3 text-white" : ""
            } transition-colors w-1/3`}
            onClick={() => setQueType("tf")}
          >
            صح أو غلط
          </button>
          <button
            type="button"
            className={` ${
              queType == "s" ? "bg-green-500 p-3 text-white" : ""
            } transition-colors w-1/3`}
            onClick={() => setQueType("s")}
          >
            سؤال وجواب
          </button>
          <button
            type="button"
            className={` ${
              queType == "mc" ? "bg-green-500 p-3 text-white" : ""
            } transition-colors w-1/3`}
            onClick={() => setQueType("mc")}
          >
            أختيار من متعدد
          </button>
        </div>
        <input
          type="text"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
          placeholder="أكتب السؤال"
        />
        <input
          type="text"
          value={image_url}
          onChange={(e) => setImage_url(e.target.value)}
          className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
          placeholder="رابط صورة السؤال"
        />
        {queType === "tf" && (
          <React.Fragment>
            <p dir="rtl">أختر الأجابة الصحيحة :</p>
            <div className="bg-gray-200 rounded-lg overflow-hidden">
              <button
                type="button"
                className={` ${
                  rightAnswer == true ? "bg-green-500 p-3 text-white" : ""
                } transition-colors w-1/2`}
                onClick={() => setRightAnswer(true)}
              >
                صح
              </button>
              <button
                type="button"
                className={` ${
                  rightAnswer == false ? "bg-green-500 p-3 text-white" : ""
                } transition-colors w-1/2`}
                onClick={() => setRightAnswer(false)}
              >
                غلط
              </button>
            </div>
          </React.Fragment>
        )}
        {queType === "s" && (
          <input
            type="text"
            value={rightAnswer}
            onChange={(e) => setRightAnswer(e.target.value)}
            className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
            placeholder="أكتب الجواب الصحيح"
          />
        )}
        {queType === "mc" && (
          <React.Fragment>
            <input
              type="text"
              value={rightAnswer}
              onChange={(e) => setRightAnswer(e.target.value)}
              className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
              placeholder="أكتب الخيار الصحيح"
            />
            <input
              type="text"
              value={wrongAnswer1}
              onChange={(e) => setWrongAnswer1(e.target.value)}
              className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
              placeholder="أكتب الخيار الغلط الاول"
            />
            <input
              type="text"
              value={wrongAnswer2}
              onChange={(e) => setWrongAnswer2(e.target.value)}
              className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
              placeholder="أكتب الخيار الغلط الثاني"
            />
            <input
              type="text"
              value={wrongAnswer3}
              onChange={(e) => setWrongAnswer3(e.target.value)}
              className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
              placeholder="أكتب الخيار الغلط الثالث"
            />
          </React.Fragment>
        )}
        <button className="bg-green-500 text-white mx-auto px-6 py-2 rounded-md">
          Edit
        </button>
      </form>
    </div>
  );
};

export default EditCardPopup;
