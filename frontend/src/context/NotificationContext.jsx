import { createContext, useContext, useState } from "react";

const NotificationContext = createContext();

export function NotificationProvider({ children }) {
  const [notification, setNotification] = useState(null);

  const showNotification = (message, type = "success") => {
    setNotification({ message, type });

    setTimeout(() => {
      setNotification('');
    }, 2000);
  };

  return (
    <NotificationContext.Provider value={{ showNotification }}>
      
      {/* Global Toast UI */}
      {notification && (
        <div className={`global-toast ${notification.type}`}>
          {notification.message}
        </div>
      )}

      {children}
    </NotificationContext.Provider>
  );
}

export function useNotification() {
  return useContext(NotificationContext);
}