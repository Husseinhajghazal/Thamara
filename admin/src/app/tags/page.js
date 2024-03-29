import React from "react";
import TagHead from "../../components/tags/TagHead";
import TagsList from "../../components/tags/TagsList";

const page = () => {
  return (
    <div className="p-5 w-full">
      <TagHead />
      <TagsList />
    </div>
  );
};

export default page;
