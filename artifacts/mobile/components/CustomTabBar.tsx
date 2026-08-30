import React from 'react';
import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Feather } from '@expo/vector-icons';
export interface CustomTabBarProps {
  state: any;
  descriptors: any;
  navigation: any;
  onOpenFocusModal?: () => void;
}

export default function CustomTabBar({
  state,
  descriptors,
  navigation,
  onOpenFocusModal,
}: CustomTabBarProps) {
  const insets = useSafeAreaInsets();

  return (
    <View style={[styles.tabBarContainer, { paddingBottom: Math.max(insets.bottom, 12) }]}>
      {/* Home Tab */}
      <Pressable
        style={styles.tabItem}
        onPress={() => {
          const event = navigation.emit({
            type: 'tabPress',
            target: state.routes[0].key,
            canPreventDefault: true,
          });
          if (state.index !== 0 && !event.defaultPrevented) {
            navigation.navigate(state.routes[0].name);
          }
        }}
      >
        <Feather
          name="home"
          size={24}
          color={state.index === 0 ? '#ED5B0A' : '#8C7C6D'}
        />
        <Text style={[styles.tabLabel, { color: state.index === 0 ? '#ED5B0A' : '#8C7C6D' }]}>
          Home
        </Text>
      </Pressable>

      {/* Focus Central Elevated Tab */}
      <View style={styles.centerTabWrapper}>
        <Pressable
          style={({ pressed }) => [
            styles.centerButton,
            { transform: [{ scale: pressed ? 0.94 : 1 }] },
          ]}
          onPress={() => {
            if (onOpenFocusModal) {
              onOpenFocusModal();
            } else if (state.routes[1]) {
              navigation.navigate(state.routes[1].name);
            }
          }}
        >
          <Image
            source={require('@/assets/images/egg_nest.png')}
            style={styles.eggImage}
            resizeMode="contain"
          />
        </Pressable>
        <Text style={[styles.tabLabel, { color: state.index === 1 ? '#ED5B0A' : '#8C7C6D', marginTop: 4 }]}>
          Focus
        </Text>
      </View>

      {/* Chat Tab */}
      <Pressable
        style={styles.tabItem}
        onPress={() => {
          const chatRoute = state.routes.find((r: any) => r.name === 'chat') || state.routes[2];
          if (chatRoute) {
            const event = navigation.emit({
              type: 'tabPress',
              target: chatRoute.key,
              canPreventDefault: true,
            });
            if (state.index !== 2 && !event.defaultPrevented) {
              navigation.navigate(chatRoute.name);
            }
          }
        }}
      >
        <Feather
          name="message-square"
          size={24}
          color={state.index === 2 ? '#ED5B0A' : '#8C7C6D'}
        />
        <Text style={[styles.tabLabel, { color: state.index === 2 ? '#ED5B0A' : '#8C7C6D' }]}>
          Chat
        </Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  tabBarContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',
    backgroundColor: '#FFF6E5',
    borderTopWidth: 1,
    borderTopColor: '#E8DFC9',
    paddingTop: 10,
    elevation: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: -3 },
    shadowOpacity: 0.05,
    shadowRadius: 8,
  },
  tabItem: {
    alignItems: 'center',
    justifyContent: 'center',
    flex: 1,
    gap: 4,
  },
  centerTabWrapper: {
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: -28,
  },
  centerButton: {
    width: 68,
    height: 68,
    borderRadius: 34,
    backgroundColor: '#FFF6E5',
    borderWidth: 3,
    borderColor: '#FDE8D0',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.12,
    shadowRadius: 8,
    elevation: 6,
    overflow: 'hidden',
  },
  eggImage: {
    width: 52,
    height: 52,
    borderRadius: 26,
  },
  tabLabel: {
    fontSize: 12,
    fontWeight: '700',
  },
});
