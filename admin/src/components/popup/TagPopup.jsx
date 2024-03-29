"use client";

import React, { useState } from "react";

const TagPopup = () => {
  const [name, setName] = useState("");

  const handleSumbit = async (e) => {
    e.preventDefault();
    fetch("https://one18-team.onrender.com/tag", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name }),
    })
      .then((response) => response.json())
      .then((data) => window.location.reload());
  };

  return (
    <div className="bg-white p-6 rounded-md absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex items-center justify-center gap-5 flex-col">
      <h2 className="text-green-500 font-semibold text-3xl">Create Tag</h2>
      <form onSubmit={handleSumbit} className="flex flex-col gap-5">
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
          placeholder="Tag name"
        />
        <button className="bg-green-500 text-white mx-auto px-6 py-2 rounded-md">
          Create
        </button>
      </form>
    </div>
  );
};

export default TagPopup;
