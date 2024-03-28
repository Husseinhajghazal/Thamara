import CollectionsList from "../components/collections/CollectionsList";
import Head from "../components/collections/ColHead";

export default function Home() {
  return (
    <div className="p-5 w-full">
      <Head />
      <CollectionsList />
    </div>
  );
}
