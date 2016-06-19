# ZLayout

This is a customized layout.

A muti-line layout which would behave as a horizontal LinearLayout but places new added child views in next line when there is no enough horizontal space.

Just like a Z !

todo list:

- 不支持水平方向的 ScrollView，仍然会换行，而非水平滚动；
- 设置高度值无效，效果仍等同于 wrap_content；
- 优化高度设置，当高度溢出时记下 child view 的个数，到达该数值停止 layout；
- 增加 maxLines 属性；
- 增加 linesSpaces 属性；
