/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileDiamondInside extends AbstractFtile {

	private final HtmlColor backColor;
	private final HtmlColor borderColor;
	private final Swimlane swimlane;
	private final TextBlock label;
	private final TextBlock west;
	private final TextBlock east;
	private final TextBlock north;
	private final TextBlock south;

	public FtileDiamondInside(boolean shadowing, HtmlColor backColor, HtmlColor borderColor, Swimlane swimlane,
			TextBlock label) {
		this(shadowing, backColor, borderColor, swimlane, label, TextBlockUtils.empty(0, 0),
				TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0));
	}

	public FtileDiamondInside withNorth(TextBlock north) {
		return new FtileDiamondInside(shadowing(), backColor, borderColor, swimlane, label, north, south, west, east);
	}

	public FtileDiamondInside withWest(TextBlock west) {
		return new FtileDiamondInside(shadowing(), backColor, borderColor, swimlane, label, north, south, west, east);
	}

	public FtileDiamondInside withEast(TextBlock east) {
		return new FtileDiamondInside(shadowing(), backColor, borderColor, swimlane, label, north, south, west, east);
	}

	public Ftile withWestAndEast(TextBlock tb1, TextBlock tb2) {
		return withWest(tb1).withEast(tb2);
	}

	public FtileDiamondInside withSouth(TextBlock south) {
		return new FtileDiamondInside(shadowing(), backColor, borderColor, swimlane, label, north, south, west, east);
	}

	private FtileDiamondInside(boolean shadowing, HtmlColor backColor, HtmlColor borderColor, Swimlane swimlane,
			TextBlock label, TextBlock north, TextBlock south, TextBlock west, TextBlock east) {
		super(shadowing);
		this.backColor = backColor;
		this.swimlane = swimlane;
		this.borderColor = borderColor;
		this.label = label;
		this.west = west;
		this.east = east;
		this.north = north;
		this.south = south;
	}

	public Set<Swimlane> getSwimlanes() {
		if (swimlane == null) {
			return Collections.emptySet();
		}
		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimLabel = label.calculateDimension(stringBounder);
		final Dimension2D dimTotal = calculateDimensionAlone(stringBounder);
		ug = ug.apply(new UChangeColor(borderColor)).apply(new UStroke(1.5)).apply(new UChangeBackColor(backColor));
		ug.draw(Diamond.asPolygon(shadowing(), dimTotal.getWidth(), dimTotal.getHeight()));

		north.drawU(ug.apply(new UTranslate(4 + dimTotal.getWidth() / 2, dimTotal.getHeight())));
		south.drawU(ug.apply(new UTranslate(4 + dimTotal.getWidth() / 2, dimTotal.getHeight())));

		final double lx = (dimTotal.getWidth() - dimLabel.getWidth()) / 2;
		final double ly = (dimTotal.getHeight() - dimLabel.getHeight()) / 2;
		label.drawU(ug.apply(new UTranslate(lx, ly)));

		final Dimension2D dimWest = west.calculateDimension(stringBounder);
		west.drawU(ug.apply(new UTranslate(-dimWest.getWidth(), -dimWest.getHeight() + dimTotal.getHeight() / 2)));

		final Dimension2D dimEast = east.calculateDimension(stringBounder);
		east.drawU(ug.apply(new UTranslate(dimTotal.getWidth(), -dimEast.getHeight() + dimTotal.getHeight() / 2)));

	}

	private FtileGeometry calculateDimensionAlone(StringBounder stringBounder) {
		final Dimension2D dimLabel = label.calculateDimension(stringBounder);
		final Dimension2D dim;
		if (dimLabel.getWidth() == 0 || dimLabel.getHeight() == 0) {
			dim = new Dimension2DDouble(Diamond.diamondHalfSize * 2, Diamond.diamondHalfSize * 2);
		} else {
			dim = Dimension2DDouble.delta(
					Dimension2DDouble.atLeast(dimLabel, Diamond.diamondHalfSize * 2, Diamond.diamondHalfSize * 2),
					Diamond.diamondHalfSize * 2, 0);
		}
		return new FtileGeometry(dim, dim.getWidth() / 2, 0, dim.getHeight());
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		final FtileGeometry dimDiamonAlone = calculateDimensionAlone(stringBounder);
		final Dimension2D dimWest = west.calculateDimension(stringBounder);
		final Dimension2D dimEast = east.calculateDimension(stringBounder);
		final double northHeight = north.calculateDimension(stringBounder).getHeight();
		return dimDiamonAlone.incHeight(northHeight);
		// return dimDiamonAlone.incHeight(northHeight).addMarginX(dimWest.getWidth(), dimEast.getWidth());
	}

}
