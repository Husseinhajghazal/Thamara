"use client";

import React, { useEffect, useState } from "react";
import Tag from "./Tag";

const TagsList = () => {
  const [tags, setTags] = useState([]);

  useEffect(() => {
    fetch("https://one18-team.onrender.com/tag", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setTags(data.tags);
      });
  }, []);

  return (
    <div className="w-full my-5 grid grid-cols-5 gap-3">
      {tags.map((e) => (
        <Tag key={e.id} name={e.value} id={e.id} />
      ))}
    </div>
  );
};

export default TagsList;
