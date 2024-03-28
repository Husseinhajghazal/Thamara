import React, { useEffect, useState } from "react";

const levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

const DeckPopup = () => {
  const [name, setName] = useState("");
  const [color, setColor] = useState("#000000");
  const [level, setLevel] = useState(0);
  const [allowShuffle, setAllowShuffle] = useState(false);
  const [col_id, setCol_id] = useState(1);

  const [collections, setCollections] = useState([]);

  useEffect(() => {
    fetch("https://one18-team.onrender.com/collection", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setCollections(data.collections);
      });
  }, []);

  const handleSumbit = async (e) => {
    e.preventDefault();
    fetch("http://localhost:5000/deck", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: name,
        image_url:
          "https://drive.google.com/drive/folders/1IUV5z3pm52liXkQF_Weo6P-UpcQo9TpC",
        color: color,
        level: level,
        col_id: col_id,
        allowShuffle: allowShuffle,
      }),
    })
      .then((response) => response.json())
      .then((data) => window.location.reload());
  };

  return (
    <div className="bg-white p-6 rounded-md absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex items-center justify-center gap-5 flex-col">
      <h2 className="text-green-500 font-semibold text-3xl">Create Deck</h2>
      <form onSubmit={handleSumbit} className="flex flex-col gap-5">
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
          placeholder="Deck name"
        />
        <select
          onChange={(e) => setCol_id(e.target.value)}
          className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
        >
          <option value={0}>أختر الكوكشن</option>
          {collections.map((e, index) => (
            <option key={index} value={e.id}>
              {e.name}
            </option>
          ))}
        </select>
        <div className="flex shadow-md rounded-md p-3">
          <button
            type="button"
            className={`${allowShuffle && "text-green-500"} w-1/2`}
            onClick={() => setAllowShuffle(true)}
          >
            السماح بالخربطة
          </button>
          <button
            type="button"
            className={`${!allowShuffle && "text-green-500"} w-1/2`}
            onClick={() => setAllowShuffle(false)}
          >
            منع الخربطة
          </button>
        </div>
        <div className="flex gap-2 justify-center">
          {levels.map((e, index) => (
            <button
              key={index}
              type="button"
              onClick={() => setLevel(e)}
              className={`w-10 h-10 rounded-full flex items-center justify-center ${
                level == e ? "bg-green-500 text-white" : "bg-slate-200"
              }`}
            >
              {e}
            </button>
          ))}
        </div>
        <input
          type="color"
          value={color}
          onChange={(e) => setColor(e.target.value)}
          className="focus:outline-none border-2 w-[500px] h-8 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
        />
        <button className="bg-green-500 text-white mx-auto px-6 py-2 rounded-md">
          Create
        </button>
      </form>
    </div>
  );
};

export default DeckPopup;
