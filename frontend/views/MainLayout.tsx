import {Suspense} from 'react';
import {NavLink, Outlet} from 'react-router-dom';
import {useRouteMetadata} from "../util/routing";
import {AppLayout, DrawerToggle} from "@vaadin/react-components";
import Placeholder from "../components/placeholder/Placeholder";

const navLinkClasses = ({ isActive }: any) => {
  return `block rounded-m p-s ${isActive ? 'bg-primary-10 text-primary' : 'text-body'}`;
};

export default function MainLayout() {
  const currentTitle = useRouteMetadata()?.title ?? 'My App';
  return (
    <AppLayout primarySection="drawer">
      <div slot="drawer" className="flex flex-col justify-between h-full p-m">
        <header className="flex flex-col gap-m">
          <h1 className="text-l m-0">AI Chat 🤖</h1>
          <nav>
            <NavLink className={navLinkClasses} to="/">
              Streaming Chat
            </NavLink>
          </nav>
        </header>
      </div>

      <DrawerToggle slot="navbar" aria-label="Menu toggle"></DrawerToggle>
      <h2 slot="navbar" className="text-l m-0">
        {currentTitle}
      </h2>

      <Suspense fallback={<Placeholder />}>
        <Outlet />
      </Suspense>
    </AppLayout>
  );
}
